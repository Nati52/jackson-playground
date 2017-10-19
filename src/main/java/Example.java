import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;
import students.Student;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
//All above are imports
/**
 * A base class that calls the Jackson code to construct itself and in the process several students.
 */
//classes and methods(1 public and the rest are to be privates)
public class Example {

	/**
	 * The best student in the class. Should also be in students.
	 */
	@NotNull
	private final Student valor;

	/**
	 * A list of all the students, including the valedictorian.
	 */
	@NotNull
	private final List<Student> students;

	/**
	 * The name of the class.
	 */
	@NotNull
	private final String classname;

	/**
	 * Default constructor
	 *
	 * @param valor The best student in the class. Should also be in students.
	 * @param students      A list of all the students, including the valor.
	 * @param classname     The name of the class.
	 */
	//Jason creator that a method or
	//a constructor is used to create an object
	@JsonCreator
	public Example(@NotNull @JsonProperty(required = true) Student valedictorian,
	               @NotNull @JsonProperty(required = true) List<Student> students,
	               @NotNull @JsonProperty(required = true) String classname) {
		this.valor = valedictorian;
		this.students = students;
		this.classname = classname;
	}
   //Takes data from yaml file and constructs it in java run forum
	/**
	 * Reads the Yaml file and uses it to construct an {@link Example}.
	 *
	 * @param args Command line arguments, not used.
	 * @throws IOException If example.yml doesn't exist or isn't a proper YAML file.
	 */
	public static void main(String[] args) throws IOException {
		Yaml yaml = new Yaml();
		Map<?, ?> normalized = (Map<?, ?>) yaml.load(new FileReader("example.yml"));
		YAMLMapper mapper = new YAMLMapper();
		//Turn the Map read by SnakeYaml into a String so Jackson can read it.
		String fixed = mapper.writeValueAsString(normalized);
		//Use a parameter name module so we don't have to specify name for every field.
		mapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
		//Deserialize the map into an object.
		Example output = mapper.readValue(fixed, Example.class);
		System.out.println("Class name: " + output.classname);
		System.out.println("Valedictorian: " + output.valor);
		System.out.println("Students: " + output.students);
		System.out.println("Valedictorian is first student in Students: " + (output.valor == output.students.get(0)));
	}
}