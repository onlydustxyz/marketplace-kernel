# Marketplace Kernel

Shared modules and utilities for the Marketplace project.

## Development guide

1. Make sure you have pulled the latest changes from the `main` branch.
2. Use the SNAPSHOT version specified in the [`pom.xml`](pom.xml) file.
3. After each code modification, run `mvn clean install` to install the module in your local Maven repository.
4. In the other repos using the kernel (e.g. `marketplace-api`), patch the version of the dependency in the `pom.xml`
   file to this current SNAPSHOT version.
5. Once you are done with your changes, create a PR and wait for the CI to pass.
6. Once the PR is merged, the CI will create a new release and publish it to the Maven GitHub repository.
7. In the other repos using the kernel, update the version of the dependency in the `pom.xml` file to the new release
   version.
8. Make a PR to update the dependency in the other repos.
