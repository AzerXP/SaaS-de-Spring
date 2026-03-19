package com.saas.spring.Token;

import com.saas.spring.User.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = true)
    @NotNull(message = "El token no puede ser nulo")
    private String token;

    private boolean isRevoked;

    private boolean isExpired;

    @Enumerated(EnumType.STRING)
    private TokenType type = TokenType.BEARER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "token_user",
            joinColumns = @JoinColumn( name = "token_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private User user;

}
