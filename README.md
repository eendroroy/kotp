# kotp

![Java CI with Gradle](https://github.com/eendroroy/kotp/workflows/Tests/badge.svg)
[![GitHub tag](https://img.shields.io/github/tag/eendroroy/kotp.svg)](https://github.com/eendroroy/kotp/tags)

[![Contributors](https://img.shields.io/github/contributors/eendroroy/kotp.svg)](https://github.com/eendroroy/kotp/graphs/contributors)
[![GitHub last commit (branch)](https://img.shields.io/github/last-commit/eendroroy/kotp/master.svg)](https://github.com/eendroroy/kotp)
[![license](https://img.shields.io/github/license/eendroroy/kotp.svg)](https://github.com/eendroroy/kotp/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/eendroroy/kotp.svg)](https://github.com/eendroroy/kotp/issues)
[![GitHub closed issues](https://img.shields.io/github/issues-closed/eendroroy/kotp.svg)](https://github.com/eendroroy/kotp/issues?q=is%3Aissue+is%3Aclosed)
[![GitHub pull requests](https://img.shields.io/github/issues-pr/eendroroy/kotp.svg)](https://github.com/eendroroy/kotp/pulls)
[![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed/eendroroy/kotp.svg)](https://github.com/eendroroy/kotp/pulls?q=is%3Apr+is%3Aclosed)

OTP generation and validation library.

* Implements [RFC4226](https://datatracker.ietf.org/doc/html/rfc4226)
  and [RFC6238](https://datatracker.ietf.org/doc/html/rfc6238)
* Compatible with [Google Authenticator](https://github.com/google/google-authenticator).

### Demo

[![asciicast](http://asciinema.org/a/352223.svg)](https://asciinema.org/a/352223)

### Installation

*Maven*

```xml

<dependency>
    <groupId>com.github.eendroroy</groupId>
    <artifactId>kotp</artifactId>
    <version>1.0.0</version>
</dependency>
```

*Gradle*

```groovy
dependencies {
    implementation 'com.github.eendroroy:kotp:1.0.0'
}
```

### ChangeSet

[ChangeSet](CHANGESET.md)

### Usage

See [Wiki](https://github.com/eendroroy/kotp/wiki)

## Contributing

Bug reports and pull requests are welcome on GitHub at [kotp](https://github.com/eendroroy/kotp) repository. This
project is intended to be a safe, welcoming space for collaboration, and contributors are expected to adhere to the
[Contributor Covenant](http://contributor-covenant.org) code of conduct.

1. Fork it ( https://github.com/eendroroy/kotp/fork )
1. Create your feature branch (`git checkout -b my-new-feature`)
1. Commit your changes (`git commit -am 'Add some feature'`)
1. Push to the branch (`git push origin my-new-feature`)
1. Create a new Pull Request

## Author

* **indrajit** - *Owner* - [eendroroy](https://github.com/eendroroy)

## License

The project is available as open source under the terms of the [MIT License](http://opensource.org/licenses/MIT).
