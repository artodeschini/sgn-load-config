package br.com.bb.sgn.service;

import br.com.bb.sgn.entidade.Usuario;
import br.com.bb.sgn.repository.UsuarioRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RequestScoped
public class SgnService {

    @Inject
    private UsuarioRepository repository;

    private static final Logger LOGGER = Logger.getLogger(SgnService.class.getName());

    public List<Usuario> findAllUsuariosByDataSource() {
        return repository.findAllUsuariosByDataSource();
    }

    public List<Usuario> findAllByPanache() {
        return repository.listAll();
    }

    public void persist(Usuario u) {
        repository.persist(u);
    }

    public long count() {
        return repository.count();
    }

    public Usuario getById(Long id) {
        Optional<Usuario> optional = repository.findByIdOptional(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            return optional.isPresent() ? optional.get() : optional.orElse(new Usuario());
        }
    }

    public Usuario update(Long id, Usuario u) {
        Usuario database = repository.findByIdOptional(id).get();
        database.setNome(u.getNome());
        database.setSobrenome(u.getSobrenome());
        database.setIdade(u.getIdade());
        //database.persist();

        repository.getEntityManager().persist(database);

        return database;
    }

    public boolean delete(Long id) {
        return repository.deleteById(id);
    }

}
