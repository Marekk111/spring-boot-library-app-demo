package com.marek.Authentication;

import com.marek.book.BookDTO;
import com.marek.shared.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private List<Role> roles;
}
