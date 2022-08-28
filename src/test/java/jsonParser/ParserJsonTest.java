package jsonParser;

import com.fasterxml.jackson.databind.ObjectMapper;
import jsonParser.dto.Person;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserJsonTest {

    ClassLoader classLoader = ParserJsonTest.class.getClassLoader();
    String jsonName = "john.json";

    @Test
    void jsonTestPlayer() throws IOException {
        InputStream is = classLoader.getResourceAsStream(jsonName);
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(is, Person.class);
        assertThat(person.getDocument().getTypeDocument()).isEqualTo("passport");
        assertThat(person.getDocument().getPassportData().getPassportNo()).isEqualTo(246824);
        assertThat(person.getDocument().getPassportData().getCode()).isEqualTo("USA");
        assertThat(person.getGivenName()).isEqualTo("John");
        assertThat(person.getSurname()).isEqualTo("Spiker");
        assertThat(person.getFullName()).isEqualTo("Spiker John");
        assertThat(person.getAge()).isEqualTo(33);
        assertThat(person.getLanguages()).contains("English");
    }
}
