package vnu.uet.moonbe.services;


import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vnu.uet.moonbe.models.Song;

import java.util.List;

@Service
public class FTService {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    public void massIndex() {
        try {
            SearchSession session = Search.session(this.manager);

            MassIndexer indexer = session.massIndexer(Song.class)
                    .threadsToLoadObjects(3);

            indexer.startAndWait();
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    @Transactional
    public List<Song> searchSong(String query) {
        SearchSession session = Search.session(this.manager);

        SearchResult<Song> result = session.search( Song.class )
                .where( f -> f.match()
                        .fields( "title", "artist" )
                        .matching( query ) )
                .fetch( 20 );

        return result.hits();
    }

}
