message(STATUS "QGC: Adding Pushpaka UTM Plugin")

# Tell QGC to use our plugin class
set_property(DIRECTORY ${CMAKE_SOURCE_DIR}
    APPEND PROPERTY COMPILE_DEFINITIONS
    QGC_CUSTOM_BUILD
    CUSTOMHEADER="PushpakaPlugin.h"
    CUSTOMCLASS=PushpakaPlugin
)

# Qt components required by this plugin (picked up by QGC's find_package)
set(CUSTOM_QT_COMPONENTS
    NetworkAuth
    CACHE INTERNAL "" FORCE
)

# Resources
list(APPEND CUSTOM_RESOURCES
    ${CMAKE_CURRENT_SOURCE_DIR}/custom.qrc
)
set(QGC_RESOURCES ${QGC_RESOURCES} ${CUSTOM_RESOURCES} CACHE STRING "" FORCE)

qt_add_library(PushpakaModule STATIC)

set(CUSTOM_SOURCES
    ${CMAKE_CURRENT_SOURCE_DIR}/src/PushpakaPlugin.h
    ${CMAKE_CURRENT_SOURCE_DIR}/src/PushpakaPlugin.cpp
    ${CMAKE_CURRENT_SOURCE_DIR}/src/UserAuthentication.h
    ${CMAKE_CURRENT_SOURCE_DIR}/src/UserAuthentication.cpp
    CACHE INTERNAL "" FORCE
)

set(CUSTOM_LIBRARIES
    PushpakaModule
    Qt6::NetworkAuth
    CACHE INTERNAL "" FORCE
)

set(CUSTOM_INCLUDE_DIRECTORIES
    ${CMAKE_CURRENT_SOURCE_DIR}/src
    CACHE INTERNAL "" FORCE
)
