require recipes-security/optee/optee-os.inc

DEPENDS += "dtc-native"

FILESEXTRAPATHS:prepend := "${THISDIR}/optee-os-4.0.0:"

SRCREV = "2a5b1d1232f582056184367fb58a425ac7478ec6"
SRC_URI += " \
    file://0001-allow-setting-sysroot-for-libgcc-lookup.patch \
    file://0002-core-Define-section-attributes-for-clang.patch \
    file://0003-optee-enable-clang-support.patch \
    file://0004-core-link-add-no-warn-rwx-segments.patch \
   "

SRC_URI:remove = " \
    file://0002-optee-enable-clang-support.patch \
    file://0003-core-link-add-no-warn-rwx-segments.patch \
"

DEFAULT_PREFERENCE = "-1"

do_install() {
    #install core in firmware
    install -d ${D}${nonarch_base_libdir}/firmware/
    install -m 644 ${B}/core/*.bin ${B}/core/tee.elf ${D}${nonarch_base_libdir}/firmware/

    #install tas in optee_armtz
    install -d ${D}${nonarch_base_libdir}/optee_armtz/
    install -m 444 ${B}/ta/*/*.ta ${D}${nonarch_base_libdir}/optee_armtz
}

do_deploy() {
    install -d ${DEPLOYDIR}/${MLPREFIX}optee
    install -m 644 ${D}${nonarch_base_libdir}/firmware/* ${DEPLOYDIR}/${MLPREFIX}optee

    install -d ${DEPLOYDIR}/${MLPREFIX}optee/ta
    install -m 644 ${B}/ta/*/*.elf ${DEPLOYDIR}/${MLPREFIX}optee/ta
}

FILES:${PN}-ta = "${nonarch_base_libdir}/optee_armtz/*"
