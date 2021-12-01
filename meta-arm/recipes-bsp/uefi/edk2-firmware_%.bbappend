COMPATIBLE_MACHINE:qemuarm64-secureboot = "qemuarm64"
COMPATIBLE_MACHINE:qemu-generic-arm64 = "qemu-generic-arm64"

DEPENDS:append:qemu-generic-arm64 = " virtual/trusted-firmware-a coreutils-native"

EDK2_BUILD_RELEASE:aarch64:qemuall = "1"

EDK2_PLATFORM:qemu-generic-arm64      = "SbsaQemu"
EDK2_PLATFORM_DSC:qemu-generic-arm64  = "Platform/Qemu/SbsaQemu/SbsaQemu.dsc"
EDK2_BIN_NAME:qemu-generic-arm64      = "SBSA_FLASH0.fd"

EDK2_PLATFORM:qemuarm64-secureboot      = "ArmVirtQemu-AARCH64"
EDK2_PLATFORM_DSC:qemuarm64-secureboot  = "ArmVirtPkg/ArmVirtQemu.dsc"
EDK2_BIN_NAME:qemuarm64-secureboot      = "QEMU_EFI.fd"

do_compile:prepend:qemu-generic-arm64() {
    mkdir -p ${B}/Platform/Qemu/Sbsa/
    cp ${RECIPE_SYSROOT}/firmware/bl1.bin ${B}/Platform/Qemu/Sbsa/
    cp ${RECIPE_SYSROOT}/firmware/fip.bin ${B}/Platform/Qemu/Sbsa/
}

do_install:append:qemu-generic-arm64() {
    install ${B}/Build/${EDK2_PLATFORM}/${EDK2_BUILD_MODE}_${EDK_COMPILER}/FV/SBSA_FLASH*.fd ${D}/firmware/
    # QEMU requires that the images be minimum of 256M in size
    truncate -s 256M ${D}/firmware/SBSA_FLASH*.fd
}
