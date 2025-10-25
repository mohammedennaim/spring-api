# Spring Core Docker Profile Issue - FIXED ✅

## Problem Identified

The project was using **Spring Framework (Spring Core)** without Spring Boot, and there was a critical configuration issue with Docker and Spring Profiles.

### Root Cause

**Spring Core does NOT automatically load profile-specific property files** like Spring Boot does.

The original configuration only had:
```java
@PropertySource("classpath:application.properties")
```

This means:
- ✅ `application.properties` was loaded
- ❌ `application-docker.properties` was **IGNORED** even when `SPRING_PROFILES_ACTIVE=docker` was set
- ❌ Profile-specific configuration was not being applied in Docker

## Solution Applied

### 1. Added Profile-Aware Property Source Loading

**File: `src/main/java/com/example/springapi/config/AppConfig.java`**

Added a second `@PropertySource` annotation with profile resolution:

```java
@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:application-${spring.profiles.active:default}.properties", 
                ignoreResourceNotFound = true)
```

This tells Spring to:
- Load base `application.properties` first
- Then load `application-{profile}.properties` based on active profile
- Fall back to `application-default.properties` if no profile is set
- Ignore if the profile-specific file doesn't exist

### 2. Added PropertySourcesPlaceholderConfigurer Bean

Added this static bean to enable placeholder resolution:

```java
@Bean
public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    configurer.setIgnoreUnresolvablePlaceholders(true);
    configurer.setIgnoreResourceNotFound(true);
    return configurer;
}
```

This bean is **required in Spring Core** to resolve `${...}` placeholders in `@PropertySource` and `@Value` annotations.

### 3. Created Default Profile Properties

**File: `src/main/resources/application-default.properties`**

Created a default profile configuration for when no profile is explicitly set (local development).

## How It Works Now

### Docker Environment
1. Docker Compose sets: `SPRING_PROFILES_ACTIVE=docker`
2. Dockerfile sets: `-Dspring.profiles.active=docker` in CATALINA_OPTS
3. Spring loads:
   - `application.properties` (base)
   - `application-docker.properties` (Docker-specific) ✅
4. Application uses correct database URL: `jdbc:postgresql://db:5432/spring_db`

### Local Environment
1. No profile set → defaults to "default"
2. Spring loads:
   - `application.properties` (base)
   - `application-default.properties` (local dev)
3. Application uses localhost database URL

## Key Differences: Spring Core vs Spring Boot

| Feature | Spring Boot | Spring Core (Framework) |
|---------|-------------|-------------------------|
| Profile property files | ✅ Automatic | ❌ Manual configuration required |
| `application-{profile}.properties` | ✅ Auto-loaded | ❌ Must configure with `@PropertySource` |
| PropertySourcesPlaceholderConfigurer | ✅ Auto-configured | ❌ Must define as bean |
| Environment variable binding | ✅ Automatic | ⚠️ Limited (needs system properties) |

## Testing the Fix

### Build and Run with Docker Compose
```bash
docker compose build
docker compose up -d
```

### Verify Profile is Active
```bash
docker compose logs app | grep -i "profile"
```

You should see the Docker profile being activated and `application-docker.properties` being loaded.

### Check Database Connection
```bash
curl http://localhost:8081/api/users
```

The application should now connect to the PostgreSQL database in the `db` container correctly.

## Files Modified

1. ✅ `src/main/java/com/example/springapi/config/AppConfig.java`
   - Added profile-aware `@PropertySource`
   - Added `PropertySourcesPlaceholderConfigurer` bean
   - Added import for `PropertySourcesPlaceholderConfigurer`

2. ✅ `src/main/resources/application-default.properties` (NEW)
   - Created default profile configuration

## Additional Notes

- This is a **Spring Framework (Core)** limitation, not a Docker issue
- Spring Boot handles this automatically, but pure Spring Framework requires manual configuration
- The fix is backward compatible and won't break existing deployments
- All environment variable overrides in Docker Compose still work correctly

## Summary

The problem was that Spring Core doesn't automatically support profile-specific property files like Spring Boot does. By adding profile-aware property source loading and the PropertySourcesPlaceholderConfigurer bean, the application now correctly loads `application-docker.properties` when running in Docker.

**Status: RESOLVED ✅**
