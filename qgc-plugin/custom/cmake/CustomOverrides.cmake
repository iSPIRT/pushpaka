set(QGC_APP_NAME "Pushpaka-QGroundControl" CACHE STRING "App Name" FORCE)

# Disable unused firmware plugin factories to keep the build lean
set(QGC_DISABLE_APM_MAVLINK OFF CACHE BOOL "Disable APM Dialect" FORCE)
set(QGC_DISABLE_APM_PLUGIN OFF CACHE BOOL "Disable APM Plugin" FORCE)
set(QGC_DISABLE_APM_PLUGIN_FACTORY OFF CACHE BOOL "Disable APM Plugin Factory" FORCE)
set(QGC_DISABLE_PX4_PLUGIN_FACTORY OFF CACHE BOOL "Disable PX4 Plugin Factory" FORCE)
