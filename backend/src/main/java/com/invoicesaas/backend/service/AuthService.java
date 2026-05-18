package com.invoicesaas.backend.service;

import com.invoicesaas.backend.dto.AuthResponse;
import com.invoicesaas.backend.dto.LoginRequest;
import com.invoicesaas.backend.dto.RegisterRequest;
import com.invoicesaas.backend.entity.User;
import com.invoicesaas.backend.entity.Workspace;
import com.invoicesaas.backend.entity.WorkspaceMember;
import com.invoicesaas.backend.repository.UserRepository;
import com.invoicesaas.backend.repository.WorkspaceMemberRepository;
import com.invoicesaas.backend.repository.WorkspaceRepository;
import com.invoicesaas.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .build();
        userRepository.save(user);

        Workspace workspace = Workspace.builder()
                .name(request.getWorkspaceName())
                .owner(user)
                .build();
        workspaceRepository.save(workspace);

        WorkspaceMember member = WorkspaceMember.builder()
                .workspace(workspace)
                .user(user)
                .role(WorkspaceMember.Role.OWNER)
                .build();
        workspaceMemberRepository.save(member);

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .workspaceId(workspace.getId())
                .workspaceName(workspace.getName())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        Workspace workspace = workspaceRepository.findByOwnerId(user.getId())
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No workspace found"));

        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .workspaceId(workspace.getId())
                .workspaceName(workspace.getName())
                .build();
    }
}