package com.saas.spring.exception;

public class UserExceptions {

    private UserExceptions(){
    }

    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(Long id){
            super("Usuario no encontrado con id : "+id);
        }

    }

    public static class RoleCreateException extends RuntimeException {
        public RoleCreateException(String reason){
            super("No se pudo crear el usuario : " + reason);
        }
        
    }

    public static class UserUpdateException extends RuntimeException {
        public UserUpdateException(Long id, String reason){
            super("No se pudo actualizar el usuario " + id + " : " + reason);
        }
        
    }

    public static class UserDeleteException extends RuntimeException {
        public UserDeleteException(Long id, String reason){
            super("No se pudo eliminar el usuario " + id + " : "+ reason);
        }
        
    }
}
