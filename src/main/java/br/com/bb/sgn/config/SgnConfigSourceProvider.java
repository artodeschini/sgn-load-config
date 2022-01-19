package br.com.bb.sgn.config;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.ConfigSourceProvider;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SgnConfigSourceProvider implements ConfigSourceProvider {

    private static final Logger LOG = Logger.getLogger(SgnConfigSourceProvider.class.getName());

    @Override
    public Iterable<ConfigSource> getConfigSources(ClassLoader forClassLoader) {

        // aqui por exempo pode ser pego alguma finalidade da Collection de de integracao do do mongo
        //Utilizando o ConfiguradorService.obterFinalidade
        String configurador = this.callRestApi("http://api-configurador.sgn.desenv.bb.com.br/integracao?finalidade=H2-LOCAL");
        LOG.info(configurador);
        JsonObject data = new JsonObject(configurador);
        JsonArray array = data.getJsonArray("data");//new JsonArray(configurador);
        //String finalidade = this.callRestApi("http://localhost/config");

        JsonObject json = array.getJsonObject(0);
        json = json.getJsonObject("dados");
        Map<String, Object> map = json.getMap();

        InMemoryConfigSource configuracoes = new InMemoryConfigSource();
        for (Map.Entry<String, Object> pares : map.entrySet()) {
            configuracoes.add(pares.getKey(), pares.getValue().toString());

            switch (pares.getKey()) {
                case "tipoConexao":
                    configuracoes.add("quarkus.datasource.db-kind", pares.getValue().toString());
                    break;
                case "url":
                    configuracoes.add("quarkus.datasource.jdbc.url", pares.getValue().toString());
                    break;
                case "usuario":
                    configuracoes.add("quarkus.datasource.username", pares.getValue().toString());
                    break;
                case "senha":
                    configuracoes.add("quarkus.datasource.password", pares.getValue().toString());
                default:
                    break;

            }
        }
        configuracoes.add("quarkus.hibernate-orm.dialect", "org.hibernate.dialect.H2Dialect");
        configuracoes.add("quarkus.datasource.jdbc.max-size", "8");
        configuracoes.add("quarkus.datasource.jdbc.min-size", "2");
        configuracoes.add("quarkus.hibernate-orm.database.generation", "drop-and-create");
        configuracoes.add("quarkus.hibernate-orm.log.sql", "true");

        return Collections.singletonList(configuracoes);
    }

    public String callRestApi(String uri) {
        HttpGet request = new HttpGet(uri);
        String callback;
        try {
            //HttpHost proxy = new HttpHost("http://170.66.49.180/proxy.pac");
            //HttpResponse response = HttpClientBuilder.create().setProxy(proxy).build().execute(request);
            HttpResponse response = HttpClientBuilder.create().build().execute(request);
            callback = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {

            LOG.log(Level.SEVERE, e.getMessage(), e);
            throw new RuntimeException("Error to call api");
        }

        return callback;
    }
}
