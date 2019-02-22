package br.com.hbsis.tonaarea.util;

public class Constants {

    public static  final class STATUS_WORK_FLOW{
        public static final String ON_APPROVAL = "Pendente";
        public static final String APPROVED = "Aprovada";
        public static final String DISAPPROVED = "Reprovada";
    }

    public static final class OPERATION_METHOD {
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    public static final class URL {
        public static final String BASE_URL ="https://homtonaarea.promaxcloud.com.br:6001/";
        public static final String URL_AUDITORIAS = "https://homtonaarea.promaxcloud.com.br:6001/api/v1/auditorias";
        public static final String URL_AUDITORIAS_QUANTIDADE = "https://homtonaarea.promaxcloud.com.br:6001/api/v1/auditorias/quantidade";
        public static final String URL_AUDITORIAS_POST = "api/v1/auditorias";
        public static final String URL_USER = "https://homtonaarea.promaxcloud.com.br:6001/api/v1/usuarios";
        public static final String URL_LOGIN = "api/v1/usuarios/login";
        public static final String URL_CLIENTES_POST = "/api/v1/clientes";
        public static final String URL_CLIENTES = "https://homtonaarea.promaxcloud.com.br:6001/api/v1/clientes";
        public static final String URL_PRODUTOS = "https://homtonaarea.promaxcloud.com.br:6001/api/v1/produtos/lista-produtos";
        public static final String URL_CLIENTES_ULTIMA_ATUALIZACAO = "https://homtonaarea.promaxcloud.com.br:6001/api/v1/clientes/ultima-atualizacao";
        public static final String URL_PRODUTOS_ULTIMA_ATUALIZACAO = "https://homtonaarea.promaxcloud.com.br:6001/api/v1/produtos/ultima-atualizacao";

    }

    public static final class SECURITY_PREFERENCES_CONSTANTS{
        public static final String USER_ID = "userid";
        public static final String REV_ID = "revId";
        public static final String USER_NAME = "nome";
        public static final String PRODUCT_DATE = "productDate";
        public static final String CLIENT_DATE = "clientDate";
        public static final String KEY_PREFS_FIRST_LAUNCH = "first_launch";
    }

    public static final class DATABASE{
        public static final String DATABASE_NAME = "TONAAREA";

        public static final class TABLE_CLIENT{
            public static final String TABLE_NAME = "CLIENT";
            public static final String COLUMN_ID = "ID";
            public static final String COLUMN_NAME = "NAME";
            public static final String COLUMN_CODE = "CODE";
            public static final String COLUMN_ACTIVE ="ACTIVE";
            public static final String COLUMN_REVENDA_NAME = "REVENDA_NAME";
            public static final String COLUMN_DATE = "DATE";

        }

        public static final class TABLE_PRODUCT{
            public static final String TABLE_NAME = "PRODUCT";
            public static final String COLUMN_NAME  = "NAME";
            public static final String COLUMN_ID = "ID";
            public static final String COLUMN_DATE = "DATE";
        }
    }


}
