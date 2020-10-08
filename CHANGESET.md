# 0.1.3
- Use Long value as otp input/counter

**Deprecations**
- `OTP.generateOtp(Int): String` (use: `OTP.generateOtp(Long): String`)
- `HOTP.at(Int): String` (use: `HOTP.at(Long): String`)
- `HOTP.verify(String, Int, Int): Int?` (use: `HOTP.verify(String, Long, Long): Long?`)
- `HOTP.provisioningUri(String, Int): String?` (use: `HOTP.provisioningUri(String, Long): String?`)
- `TOTP.verify(String, Int, Int, Date?, Date): Int?` (use: `TOTP.verify(String, Long, Long, Date?, Date): Long?`)

# 0.1.2
- Added `Base32` and `Base32String` classes
- `HOTP` and `TOTP` secrets are now `Base32String`
- Added `HOTPConfig` and `TOTPConfig`

**Deprecations**
- `HOTP(Base32String, Int, Digest)` (use: `HOTP(HOTPConfig)`)
- `TOTP(Base32String, Int, Digest, Int, String)` (use: `HOTP(TOTPConfig)`)