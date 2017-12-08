module org.kjussakov.sochorn.samples {
    // This export is only needed for exposing the Echo.done() callback to the Nashorn engine
    exports org.kjussakov.sochorn.samples;

    requires java.scripting;
    requires org.kjussakov.sochorn;
}