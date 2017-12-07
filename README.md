# Compile the two modules

javac -d mods --module-source-path src $(find src -name "*.java")

# Copy the resources

cp -r src/org.kjussakov.sochorn.samples/resources/ mods/org.kjussakov.sochorn.samples/resources

# Run the Echo sample application

java --module-path mods -m org.kjussakov.sochorn.samples/org.kjussakov.sochorn.samples.Echo