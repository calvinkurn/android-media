#!/bin/bash
# change config to bits
if grep 'resRepo=tokopedia' "gradle.properties"; then
  echo "changing resRepo to bits.."
  sed -i 's/resRepo=tokopedia/resRepo=bits/g' "gradle.properties"
fi
if grep 'distributionUrl=https\\://services.gradle.org/distributions/gradle-8.2-bin.zip' gradle/wrapper/gradle-wrapper.properties; then
  echo "changing distributionUrl to bits.."
  sed -i 's|https\\://services.gradle.org/distributions/gradle-8.2-bin.zip|https://voffline.byted.org/download/tos/schedule/ttclient-android/gradle/gradle-8.2-all.zip|g' gradle/wrapper/gradle-wrapper.properties
  cat gradle/wrapper/gradle-wrapper.properties
fi

if [ -f "settings.gradle" ]; then
  echo "disabling unused settings for $1.."
  if [ "$1" == "customerapp" ]; then
    if ! grep "//apply from: \"buildconfig/appcompile/compile-testapp.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-testapp.gradle\"|//apply from: \"buildconfig/appcompile/compile-testapp.gradle\"|g' settings.gradle
    fi
    if ! grep "//apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"|//apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"|g' settings.gradle
    fi
    if ! grep "//apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"|//apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"|g' settings.gradle
    fi
    if ! grep "//apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"|//apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"|g' settings.gradle
    fi
    if ! grep "//apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"|//apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"|g' settings.gradle
    fi

    if grep "//apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"" settings.gradle; then
      sed -i 's|//apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"|apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"|g' settings.gradle
    fi
    if grep "//apply from: \"buildconfig/appcompile/compile-libraries.gradle\"" settings.gradle; then
      sed -i 's|//apply from: \"buildconfig/appcompile/compile-libraries.gradle\"|apply from: \"buildconfig/appcompile/compile-libraries.gradle\"|g' settings.gradle
    fi
  fi
  if [ "$1" == "sellerapp" ]; then
    if ! grep "//apply from: \"buildconfig/appcompile/compile-testapp.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-testapp.gradle\"|//apply from: \"buildconfig/appcompile/compile-testapp.gradle\"|g' settings.gradle
    fi
    if ! grep "//apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"|//apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"|g' settings.gradle
    fi
    if ! grep "//apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"|//apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"|g' settings.gradle
    fi
    if ! grep "//apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"" settings.gradle; then
      sed -i 's|apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"|//apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"|g' settings.gradle
    fi

    if grep "//apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"" settings.gradle; then
      sed -i 's|//apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"|apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"|g' settings.gradle
    fi
    if grep "//apply from: \"buildconfig/appcompile/compile-libraries.gradle\"" settings.gradle; then
      sed -i 's|//apply from: \"buildconfig/appcompile/compile-libraries.gradle\"|apply from: \"buildconfig/appcompile/compile-libraries.gradle\"|g' settings.gradle
    fi
    if grep "//apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"" settings.gradle; then
      sed -i 's|//apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"|apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"|g' settings.gradle
    fi
    if [ "$1" == "testapp" ]; then
      if ! grep "//apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"" settings.gradle; then
        sed -i 's|apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"|//apply from: \"buildconfig/appcompile/compile-sellerapp.gradle\"|g' settings.gradle
      fi
      if ! grep "//apply from: \"buildconfig/appcompile/compile-libraries.gradle\"" settings.gradle; then
        sed -i 's|apply from: \"buildconfig/appcompile/compile-libraries.gradle\"|//apply from: \"buildconfig/appcompile/compile-libraries.gradle\"|g' settings.gradle
      fi
      if ! grep "//apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"" settings.gradle; then
        sed -i 's|apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"|//apply from: \"buildconfig/appcompile/compile-customerapp_pro.gradle\"|g' settings.gradle
      fi
      if ! grep "//apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"" settings.gradle; then
        sed -i 's|apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"|//apply from: \"buildconfig/appcompile/compile-customerapp.gradle\"|g' settings.gradle
      fi
      if ! grep "//apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"" settings.gradle; then
        sed -i 's|apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"|//apply from: \"buildconfig/appcompile/compile-wearosapp.gradle\"|g' settings.gradle
      fi

      if grep "//apply from: \"buildconfig/appcompile/compile-testapp.gradle\"" settings.gradle; then
        sed -i 's|//apply from: \"buildconfig/appcompile/compile-testapp.gradle\"|apply from: \"buildconfig/appcompile/compile-testapp.gradle\"|g' settings.gradle
      fi
      if grep "//apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"" settings.gradle; then
        sed -i 's|//apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"|apply from: \"buildconfig/appcompile/compile-testapp-plugin.gradle\"|g' settings.gradle
      fi
    fi
    echo "done disabling unused settings"
  fi
fi
cat settings.gradle
