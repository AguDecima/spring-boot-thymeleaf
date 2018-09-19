package com.decima.spring.entidad.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.decima.spring.entidad.models.entity.Cliente;
import com.decima.spring.entidad.models.service.IClienteService;
import com.decima.spring.entidad.models.service.IUploadService;
import com.decima.spring.entidad.util.paginator.PageRender;

@Controller
// esta anotacion sirve para mantener los datos del objeto indicado en la sesion hasta destruirla
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteServ;

	@Autowired
	private IUploadService uploadService;

	// para el debug en consola y mostrar los nombres de los archivos fotos
	//private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	// sirve para cargar una imagen automaticamente por http
	// busca en la carpeta de recurso donde coincida el nombre de la imagen
	@GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {
		Resource recurso = null;
		try {
			recurso = uploadService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Optional<Cliente> cliente = clienteServ.findOne(id);
		if (!cliente.isPresent()) {
			flash.addAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		// desenvuelvo el objeto que se encuentra en el opional y lo agrego a mi modelo
		cliente.ifPresent(o -> ((Model) model).addAttribute("cliente", o));

		return "ver";
	}

	@RequestMapping(value = { "/", "/listar" }, method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		// recibo el numero de pagina y la cantidad de datos que deseo mostrar por
		// pagina
		Pageable pageRequest = PageRequest.of(page, 5);
		// guardo en un tipo page los clientes para mostrar
		Page<Cliente> clientes = clienteServ.findall(pageRequest);
		// determino como mostrar las paginas en la vista
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("titulo", "Lista de Contactos");
		model.addAttribute("clientes", clientes);
		model.addAttribute("page", pageRender);
		return "listar";
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model) {
		Cliente cliente = new Cliente();
		model.put("titulo", "Formulario de Cliente");
		model.put("cliente", cliente);
		return "form";
	}

	// RedirectAttributes sirve para mostrar msj flash
	@RequestMapping(value = "/form/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Optional<Cliente> cliente = null;

		if (id > 0) {
			cliente = clienteServ.findOne(id);
			if (cliente == null) {
				flash.addFlashAttribute("error", "El cliente no existe en la base de datos!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("error", "El Id no puede ser cero!");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST)
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors()) {
			model.addAttribute("titulo", "Formulario del cliente");
			return "form";
		}

		if (!foto.isEmpty()) {
			// si existe una foto y el cliente desea cambiarla
			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0) {
				uploadService.delete(cliente.getFoto());
			}

			String uniqueFilename = null;
			try {
				uniqueFilename = uploadService.copy(foto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Has subido la foto " + foto.getOriginalFilename() + " correctamente.!");
			cliente.setFoto(uniqueFilename);
		}
		String msjFlash = (cliente.getId() != null) ? "El cliente fue editado con exito!"
				: "El cliente fue creado con exito!";

		clienteServ.save(cliente);
		// completo la sesion y ya no guarda los datos del cliente
		status.setComplete();
		flash.addFlashAttribute("success", msjFlash);
		return "redirect:listar";
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Optional<Cliente> c = clienteServ.findOne(id);

			clienteServ.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito!");

			if (uploadService.delete(c.get().getFoto()))
				;
			{
				flash.addFlashAttribute("info", "Foto " + c.get().getFoto() + " eliminada.!");
			}

		}
		return "redirect:/listar";
	}

}
