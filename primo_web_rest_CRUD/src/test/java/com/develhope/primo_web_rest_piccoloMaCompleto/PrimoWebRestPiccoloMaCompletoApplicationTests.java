package com.develhope.primo_web_rest_piccoloMaCompleto;

import com.develhope.primo_web_rest_piccoloMaCompleto.controllers.StudentController;
import com.develhope.primo_web_rest_piccoloMaCompleto.entities.Student;
import com.develhope.primo_web_rest_piccoloMaCompleto.services.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PrimoWebRestPiccoloMaCompletoApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private StudentController controller;

	@Autowired
	private StudentService service;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void doesControllerExist(){
		assertThat(controller).isNotNull();
	}
	@Test
	public void doesServiceExist(){
		assertThat(service).isNotNull();
	}

//	@Test
//	public void StudentTest () throws Exception{
//		mockMvc.perform(get("/v1/getStudent/1")).andDo(print())
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.id").value(1))
////				.andExpect(jsonPath("$.name").value(null))
////				.andExpect(jsonPath("$.surname").value("null"))
//		;
//	}

	private Student createAStudent() throws Exception{
		Student student = new Student();
		student.setWorking(true);
		student.setName("Luca");
		student.setSurname("Imperatore");

		return createAStudent(student);

	}

	private Student createAStudent(Student student) throws Exception{
		MvcResult result= createStudentRequest(student);
		Student responseStudent = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
		return responseStudent;
	}

	private MvcResult createStudentRequest() throws Exception{
		Student student = new Student();
		student.setWorking(true);
		student.setName("Luca");
		student.setSurname("Imperatore");

		return createStudentRequest(student);

	}

	private MvcResult createStudentRequest(Student student) throws Exception{
		if(student == null)	return null;

		String studentJSON = objectMapper.writeValueAsString(student);

		return this.mockMvc.perform(post("/v1/create")
						.contentType(MediaType.APPLICATION_JSON)
						.content(studentJSON))
				.andExpect(status().isOk()).andDo(print())
				.andReturn();
	}

	private Student getStudentFromId(Long id) throws Exception {
		MvcResult result = this.mockMvc.perform(get("/v1/getStudent/"+id))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		if (result.getResponse().getContentLength()==0) return null;
		return objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);
	}

	@Test
	void createAStudentTest() throws Exception{

		Student studentResponse= createAStudent();
		assertThat(studentResponse.getId()).isNotNull();
	}

	@Test
	void readList() throws Exception{
		createAStudentTest();
		createAStudentTest();
		createAStudentTest();
		createAStudentTest();
		MvcResult result= this.mockMvc.perform(get("/v1/getAll"))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		List<Student> responseStudent = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
		System.out.println("User in database are: "+ responseStudent.size() );
		assertThat(responseStudent.size()).isNotZero();
	}

	@Test
	void readSingleStudent() throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		Student responseStudent = getStudentFromId(student.getId());

		assertThat(responseStudent.getId()).isEqualTo(student.getId());
	}

	@Test
	void updateStudent()throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		String newName= "Luca del romano Impero";
		student.setName(newName);

		String studentJSON = objectMapper.writeValueAsString(student);

		MvcResult result = this.mockMvc.perform(put("/v1/putStudent/"+student.getId())
						.contentType(MediaType.APPLICATION_JSON) 	//Tipologia di contenuto
						.content(studentJSON))						// Ti mando anche il body, il JSON
				.andDo(print())										// E fai, stampa le richieste
				.andExpect(status().isOk())							// La chiamta dev'essere positiva [200]
				.andReturn(); 										// mi torna tutto il risultato con mockMvc

		Student responseStudent = objectMapper.readValue(result.getResponse().getContentAsString(), Student.class);

		//controllo dopo il put
		assertThat(responseStudent.getId()).isEqualTo(student.getId());
		assertThat(responseStudent.getName()).isEqualTo(newName);

		//controllo aggiuntivo finita l'operazione usando il get
		Student responseStudentGet = getStudentFromId(student.getId());
		assertThat(responseStudentGet.getId()).isEqualTo(student.getId());
		assertThat(responseStudentGet.getName()).isEqualTo(newName);
	}

	@Test
	void deleteUser()throws Exception{
		Student student = createAStudent();
		assertThat(student.getId()).isNotNull();

		this.mockMvc.perform(delete("/v1/delete/"+student.getId()))
				.andDo(print())
				.andExpect(status().isOk());


		Student responseStudent = getStudentFromId(student.getId());

		assertThat(responseStudent).isNull();


	}

}
