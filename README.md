# Robotium Extensions

Extension of robotium-solo library. ExtSolo extends Solo class and makes testing easier.
ExtSolo is reporting executed steps to file metadata.json under /sdcard/test-screenshots 
when test is executed in Testdroid Cloud

It requires robotium-solo included in the project.

## Usage
It's as simply as replacing Solo with ExtSolo and changing initialization:

<pre>
ExtSolo solo = new ExtSolo(getInstrumentation(), getActivity(), this.getClass().getCanonicalName(), getName());
</pre>

## Build with maven:
mvn clean install -Dandroid.sdk.path=path_to_sdk

## License

See the [LICENSE](LICENSE) file.