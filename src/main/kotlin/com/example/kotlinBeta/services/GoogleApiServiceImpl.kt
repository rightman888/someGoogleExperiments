package com.example.kotlinBeta.services

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.store.FileDataStoreFactory
import com.google.api.services.docs.v1.Docs
import com.google.api.services.docs.v1.DocsScopes
import com.google.api.services.drive.Drive
import com.google.api.services.drive.DriveScopes
import com.google.api.services.drive.model.Permission
import org.springframework.stereotype.Service
import java.io.File
import java.io.InputStreamReader

@Service
class GoogleApiServiceImpl: GoogleApiService {

    private val factory = JacksonFactory.getDefaultInstance()

    val scopes: List<String> = listOf(DocsScopes.DOCUMENTS, DriveScopes.DRIVE)
    val TOKENS_DIR_PATH = "tokens"
    val APP_NAME = "Super Name"
    val DOCID = "1hBeXIThmhCGrmnpWZ49TNNSWTBNpIUQSW4MdxqmGtYI"
    val creds = """{"installed":{"client_id":"897026268348-tnah1qbaq6gk54lvo4rhhqu3slkqqsob.apps.googleusercontent.com","project_id":"phonic-operand-265210","auth_uri":"https://accounts.google.com/o/oauth2/auth","token_uri":"https://oauth2.googleapis.com/token","auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs","client_secret":"9RDYKPMiiUe8TuPyy8cedJCb","redirect_uris":["urn:ietf:wg:oauth:2.0:oob","http://localhost"]}}"""

    override fun getDoc():String {
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val service = Docs.Builder(transport, factory, getCredentials(transport)).setApplicationName(APP_NAME).build()

        val resp = service.documents().get(DOCID).execute()
        return resp.toPrettyString()
    }

    override fun getFiles(): String {
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val service = Drive.Builder(transport, factory, getCredentials(transport)).setApplicationName(APP_NAME).build()
        val result = service.Files().list().setPageSize(10).setFields("files(id,name)").execute()
        val files = result.files

        return if (files.isNullOrEmpty()) {
            ""
        } else {
            files.map { " " + it.name + " : " + it.id + " "}.toString()
        }
    }

    override fun shareDoc(): String {
        val transport = GoogleNetHttpTransport.newTrustedTransport()
        val service = Drive.Builder(transport, factory, getCredentials(transport)).setApplicationName(APP_NAME).build()

        val userPerm = Permission().setType("user").setRole("writer").setEmailAddress("rightman777@gmail.com")
        service.permissions().create(DOCID, userPerm).setFields("id").execute()
        return ""

    }

    fun getCredentials(transport: NetHttpTransport): Credential {

        val inps = creds.byteInputStream()
        var secrets = GoogleClientSecrets.load(factory, InputStreamReader(inps))


        val fileDataStoreFactory = FileDataStoreFactory(File(TOKENS_DIR_PATH))

        val flow = GoogleAuthorizationCodeFlow
                .Builder(transport, factory, secrets, scopes)
                .setDataStoreFactory(fileDataStoreFactory)
                .setAccessType("offline").build()

        val receaver = LocalServerReceiver.Builder().setPort(8888).build()

        val authorizationCodeInstalledApp = AuthorizationCodeInstalledApp(flow, receaver)


        return authorizationCodeInstalledApp.authorize("user")
    }

}
