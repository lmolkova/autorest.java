# Install and Test on Cadl-Java

## Install Cadl

Install Node.js and [Cadl](https://github.com/microsoft/cadl/).

```shell
npm install -g @cadl-lang/compiler
```

## Build JAR

`mvn package -P local,cadl` in project root.

This will build `./cadl-extension`, which is basically `preprocessor` and `javagen` combined.

## Build and Install Cadl-Java

`pwsh Setup.ps1` in `./cadl-tests` folder.

It makes the npm package in `./cadl-extension`, then install it to `./cadl-tests` folder.

## Generate Code

`cadl compile <target.cadl>` in `./cadl-tests` folder.

Generated code will be at `./cadl-tests/cadl-output/` folder.

## Troubleshooting

### New version of `@cadl-lang/compiler` etc.

Force an installation of new version via deleting `package-lock.json` and `node_modules` in `./cadl-extension` folder.

```shell
rm -rf node_modules
rm package-lock.json
```
