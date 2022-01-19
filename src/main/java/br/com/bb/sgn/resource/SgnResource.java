package br.com.bb.sgn.resource;

import br.com.bb.sgn.entidade.Usuario;
import br.com.bb.sgn.service.SgnService;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@Path("/sgn")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SgnResource {

    private static final Logger LOG = Logger.getLogger(SgnResource.class.getName());

    public static final String TIPO_CONEXAO = "tipoConexao";

    @Inject
    private SgnService service;

    @ConfigProperty(name = TIPO_CONEXAO)
    private String tipoConexao;

    @GET
    @Produces(MediaType.TEXT_PLAIN)


    public Response works() {
        var sb = new StringBuilder("App subiu valodr da configuracao ").append(TIPO_CONEXAO).append(tipoConexao);
        return Response.ok(sb).build();
    }

    @GET
    @Path("/conf/{nome}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response mostraValorConfiguracao(@PathParam("nome") String nome) {
        String valor = ConfigProvider.getConfig().getValue(nome, String.class);
        return Response.ok(valor).build();
    }

    @GET
    @Path("/jdbc")
    public Response findJdbcAgrolDataSource() {
        return Response.ok(service.findAllUsuariosByDataSource()).build();
    }

    @GET
    @Path("/panache")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testFindAllPanache() {
        return Response.ok(service.findAllByPanache()).build();
    }

    @POST
    @Transactional
    public Response create(Usuario u) {
        service.persist(u);
        return Response.accepted().build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        return Response.ok(service.getById(id)).build();
    }

    @GET
    @Path("/count")
    @Produces(MediaType.TEXT_PLAIN)
    public Response count() {
        return Response.ok(service.count()).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response put(@PathParam("id") Long id, Usuario usuario) {
        Usuario update = service.update(id, usuario);
        return Response.ok(update).build();
    }

    @DELETE
    public Response delete(@PathParam("id") Long id) {
        try {
            service.delete(id);
            return Response.ok().build();
        } catch (Exception e) {
            LOG.severe(e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
