package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.dto.CredentialDto;
import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserService userService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getUserCredentials(String userName) {
        return credentialMapper.findCredentialByUserId(userService.getUser(userName).getUserId());
    }

    public void addCredential(CredentialDto credentialDto, String userName) {
        String encodedKey = getEncodedKey();
        Credential credential = new Credential(credentialDto.getUrl(), credentialDto.getUsername(), encodedKey,
                encryptionService.encryptValue(credentialDto.getPassword(), encodedKey), userService.getUser(userName).getUserId());
        credentialMapper.insertCredentials(credential);
    }

    public boolean updateCredential(CredentialDto credentialDto, String userName) {
        Credential credential = credentialMapper.findByCredentialIdAndUserId(credentialDto.getCredentialId(), userService.getUser(userName).getUserId());
        if (credential != null) {
            String encodedKey = getEncodedKey();
            credential.setUrl(credentialDto.getUrl());
            credential.setUsername(credentialDto.getUsername());
            credential.setKey(encodedKey);
            credential.setPassword(encryptionService.encryptValue(credentialDto.getPassword(), encodedKey));
            credentialMapper.updateCredentials(credential);
            return true;
        }
        return false;
    }

    public boolean deleteCredential(Integer credentialId, String userName) {
        Credential credential = credentialMapper.findByCredentialIdAndUserId(credentialId, userService.getUser(userName).getUserId());
        if (credential != null) {
            this.credentialMapper.deleteCredentials(credentialId);
            return true;
        }
        return false;
    }

    private String getEncodedKey() {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
