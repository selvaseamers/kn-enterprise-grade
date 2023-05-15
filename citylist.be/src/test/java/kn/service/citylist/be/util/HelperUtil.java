package kn.service.citylist.be.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class HelperUtil {

   public static Authentication ALLOW_VIEW = new Authentication() {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of((GrantedAuthority) () -> "ALLOW_VIEW");
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

        }
    };

    public static Authentication ALLOW_EDIT = new Authentication() {
        @Override
        public String getName() {
            return null;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of((GrantedAuthority) () -> "ALLOW_EDIT");
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return null;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

        }
    };

}
