#### data-plane

```yaml
azure-arm: false

license-header: MICROSOFT_MIT_SMALL

generate-client-interfaces: false
generate-client-as-impl: true
generate-sync-async-clients: true
generate-builder-per-client: true
sync-methods: all

use-default-http-status-code-to-exception-type-mapping: true
polling: {}

models-subpackage: implementation.models
client-logger: true
required-fields-as-ctor-args: true
```
