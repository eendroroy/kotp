# kotp

OTP generation and validation library.

* Implements [RFC4226](https://datatracker.ietf.org/doc/html/rfc4226)
  and [RFC6238](https://datatracker.ietf.org/doc/html/rfc6238)
* Compatible with [Google Authenticator](https://github.com/google/google-authenticator).
* Supports Alphanumeric OTP generation.
* Supports `HmacSHA1`, `HmacSHA256` and `HmacSHA512` digests.

### Demo

[![asciicast](http://asciinema.org/a/352223.svg)](https://asciinema.org/a/352223)

### Installation

*Maven*

```xml

<dependency>
    <groupId>io.github.eendroroy</groupId>
    <artifactId>kotp</artifactId>
    <version>2.0.2</version>
</dependency>
```

*Gradle*

```groovy
dependencies {
    implementation("io.github.eendroroy:kotp:2.0.2")
}
```

_Note:_ Check [releases](https://github.com/eendroroy/kotp/releases) for latest version.

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
