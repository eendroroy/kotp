# 0.1.2
- Added `Base32` and `Base32String` classes
- `HOTP` and `TOTP` secrets are now `Base32String`
- Added `HOTPConfig` and `TOTPConfig`

**Deprecations**
- instead of `HOTP(Base32String, Int, Digest)` use: `HOTP(HOTPConfig)`
- instead of `TOTP(Base32String, Int, Digest, Int, String)` use: `HOTP(TOTPConfig)`