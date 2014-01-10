package illyfrancis.pecuniary.config;

import illyfrancis.pecuniary.resource.AccountResource;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.google.common.collect.Sets;

@ApplicationPath("/services/*")
public class ServicesConfig extends Application {
//    @Override
//    public Set<Object> getSingletons() {
//        AccountResource accountResource = new AccountResource();
//        Set<Object> s = Sets.newHashSet();
//        s.add(accountResource);
//        return s;
//    }
}
