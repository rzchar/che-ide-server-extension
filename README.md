# Che IDE Server Extension Sample

## Build

version 6.16.0

cd to the project folder
`mvn clean install`

# Run

Run this sample by mounting assembly to your Che Docker image:

`docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -v {data_folder}:/data -v {project_absolute_path}/assembly/assembly-main/target/eclipse-che-6.16.0/eclipse-che-6.16.0:/assembly -v /root/.m2:/home/user/.m2 eclipse/che:6.16.0 start`

Example of running this command
`docker run -it --rm -v /var/run/docker.sock:/var/run/docker.sock -v /root/che.data.16:/data -v /root/qyd-ide-server-extension/assembly/assembly-main/target/eclipse-che-6.16.0/eclipse-che-6.16.0:/assembly -v /root/.m2:/home/user/.m2 eclipse/che:6.16.0 start`
