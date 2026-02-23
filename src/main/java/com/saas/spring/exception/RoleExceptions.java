package com.saas.spring.exception;

public class RoleExceptions {

    private RoleExceptions(){

    }

    public static class RoleNotFoundException extends RuntimeException {
        public RoleNotFoundException(Long id){
            super("Rol no encontrado con id : "+id);
        }

    }

    public static class RoleCreateException extends RuntimeException {
        public RoleCreateException(String reason){
            super("No se pudo crear el rol : " + reason);
        }
        
    }

    public static class RoleUpdateException extends RuntimeException {
        public RoleUpdateException(Long id, String reason){
            super("No se pudo actualizar el rol " + id + " : " + reason);
        }
        
    }

    public static class RoleDeleteException extends RuntimeException {
        public RoleDeleteException(Long id, String reason){
            super("No se pudo eliminar el rol " + id + " : "+ reason);
        }
        
    }

}
