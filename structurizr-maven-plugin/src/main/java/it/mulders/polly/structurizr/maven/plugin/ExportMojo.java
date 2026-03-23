package it.mulders.polly.structurizr.maven.plugin;

import com.structurizr.Workspace;
import com.structurizr.dsl.StructurizrDslParser;
import com.structurizr.dsl.StructurizrDslParserException;
import com.structurizr.export.Diagram;
import com.structurizr.export.DiagramExporter;
import com.structurizr.export.plantuml.StructurizrPlantUMLExporter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.StringJoiner;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "export", threadSafe = true, defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class ExportMojo extends AbstractMojo {

    @Parameter(name = "inputDsl", required = true)
    Path inputDsl;

    @Parameter(name = "outputDir", required = true)
    Path outputDir;

    @Parameter(name = "format", defaultValue = "plantuml")
    String format;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Workspace workspace = loadAndParseDsl();

        DiagramExporter exporter = loadDiagramExporter();

        // output
        try {
            if (!Files.exists(outputDir)) {
                Files.createDirectories(outputDir);
            }

            Collection<Diagram> diagrams = exporter.export(workspace);

            for (Diagram diagram : diagrams) {
                Path diagramFile = this.outputDir.resolve(diagram.getKey() + ".puml");
                Files.writeString(diagramFile, diagram.getDefinition());
                getLog().info("Exported diagram: " + diagramFile);
            }
        } catch (IOException ioEx) {
            throw new MojoFailureException("Failed to write diagram files", ioEx);
        }
    }

    private DiagramExporter loadDiagramExporter() throws MojoExecutionException {
        DiagramExporter exporter =
                switch (format) {
                    case "plantuml" -> new StructurizrPlantUMLExporter();
                    default -> throw new MojoExecutionException("Unsupported format: " + format);
                };

        getLog().debug("Exporter: " + exporter.getClass().getCanonicalName());

        return exporter;
    }

    private Workspace loadAndParseDsl() throws MojoFailureException {
        if (!Files.isRegularFile(this.inputDsl)) {
            throw new MojoFailureException("Input file is not a regular file: " + this.inputDsl);
        }

        try {
            // 1. Load and parse the DSL
            StructurizrDslParser parser = new StructurizrDslParser();
            parser.parse(this.inputDsl.toFile());
            return parser.getWorkspace();
        } catch (StructurizrDslParserException e) {
            throw new MojoFailureException(e);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ExportMojo.class.getSimpleName() + "[", "]")
                .add("super='" + super.toString() + "'")
                .add("inputDir=" + inputDsl)
                .add("outputDir=" + outputDir)
                .add("format='" + format + "'")
                .toString();
    }
}
