package br.com.bb.sgn.repository;

import br.com.bb.sgn.entidade.Usuario;
import io.agroal.api.AgroalDataSource;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    private static final Logger LOGGER = Logger.getLogger(UsuarioRepository.class.getName());

    @Inject
    private AgroalDataSource dataSource;

    public List<Usuario> findAllUsuariosByDataSource() {
        List<Usuario> dados = new ArrayList<>();
        try (PreparedStatement preparedStatement = dataSource.getConnection().prepareStatement("SELECT id, nome, sobrenome, idade from Usuario ")) {
            ResultSet rs = preparedStatement.executeQuery();
            Usuario usuario;
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSobrenome(rs.getString("sobrenome"));
                usuario.setIdade(rs.getInt("idade"));

                dados.add(usuario);
            }
        } catch (SQLException e) {
            LOGGER.severe(e.getMessage());
        }
        return dados;
    }
}
