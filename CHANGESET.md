# 2.0.0

- Moved from `com.github.eendroroy` to `io.github.eendroroy`

# 1.0.2

- Added `Algorithm`
- `[Braking change]` Deprecated `Digest` (Use `Algorithm`)
- `[Braking change]` Renamed `UnsupportedDigitsForProvisioningUri` to `UnsupportedOtpLengthForProvisioningUri`
- `[Braking change]` Renamed `UnsupportedDigestForProvisioningUri` to `UnsupportedAlgorithmForProvisioningUri`
- Renamed `digits` to `length`
- Renamed `conf` to `configuration` in `TOPT` and `HOTP` constructors
- Renamed `time` parameter to `epochSeconds` in `TOPT.at(Long): String`
- Renamed `count` parameter to `counter` in `HOPT.at(Long): String`

# 1.0.1

- Added `TOTP.at(Long): String`
- Added `TOTP.verify(String, Long, Long?, Long, Long): Long?`
- `[Braking change]` Removed `TOTP.verify(String, Long, Long, Date?, Date): Long?`
- `[Braking change]` Removed `TOTP.at(Date): String`
- `[Braking change]` Removed `Base32` and `Base32String` and merged all functionality in newly introduced `Secret`
- Removed `OTP.generateOtp(Int): String`
- Removed `HOTP(Base32String, Int, Digest)`
- Removed `HOTP.at(Int): String`
- Removed `HOTP.verify(String, Int, Int): Int?`
- Removed `HOTP.provisioningUri(String, Int): String?`
- Removed `TOTP(Base32String, Int, Digest, Int, String)`
- Removed `TOTP.verify(String, Int, Int, Date?, Date): Int?`

# 1.0.0

- Add supports for `radix` of OTP value, now it is possible to generate alphanumeric OTP
- Fix `provisioningUri`, now validates explicitly for Google Authenticator support

# 0.1.4

- `RFC4226` and `RFC6238` compliance
- Added support for `SHA256` and `SHA512`

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
