require recipes-security/optee/optee-test.inc

SRCREV = "1c3d6be5eaa6174e3dbabf60928d15628e39b994"

# Include ffa_spmc test group if the SPMC test is enabled.
# Supported after op-tee v3.20
EXTRA_OEMAKE:append = "${@bb.utils.contains('MACHINE_FEATURES', 'optee-spmc-test', \
                                        ' CFG_SPMC_TESTS=y CFG_SECURE_PARTITION=y', '' , d)}"

RDEPENDS:${PN} += "${@bb.utils.contains('MACHINE_FEATURES', 'optee-spmc-test', \
                                              ' arm-ffa-user', '' , d)}"

inherit deploy

DEFAULT_PREFERENCE = "-1"

EXTRA_OEMAKE:append = " \
			OPTEE_OPENSSL_EXPORT=${STAGING_INCDIR} \
"

CFLAGS:append = " -Wno-error=deprecated-declarations"
DEPENDS:append = " openssl"

do_deploy () {
    install -d ${DEPLOYDIR}/${MLPREFIX}optee/ta
    install -m 644 ${B}/ta/*/*.elf ${DEPLOYDIR}/${MLPREFIX}optee/ta
}

addtask deploy before do_build after do_install
