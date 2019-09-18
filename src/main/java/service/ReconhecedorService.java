package service;

import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import dominio.RespostaDTO;

@Stateless
@Path("/v1")
public class ReconhecedorService {

	@GET
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/compilar")
	public RespostaDTO compilar() {
		RespostaDTO resposta = new RespostaDTO();
		resposta.setPercentualConfiabilidade(10);
		return resposta;
	}
	
}
