package com.mycompany.myapp.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);
    private static JavaMailSender javaMailSender;
    private static Session session = null;
    static String origen = "preciadotattooink@gmail.com";
    static String pass = "preciadotattoo";
    static String smtp = "smtp.gmail.com";
    static String puertoTLS = "587";
    static String puertoSSL = "465";
    static Boolean gmail = Boolean.TRUE;

    public static void sendArchivoTLS(List<File> files, String correo, String encabezado, String mensaje) {
        emailSetupTLS();
        sendAdjuntoEmail(correo, encabezado, mensaje, files);
    }

    public static void sendArchivoSSL(List<File> files, String correo, String encabezado, String mensaje) {
        emailSetupSSL();
        sendAdjuntoEmail(correo, encabezado, mensaje, files);
    }

    private static void emailSetupTLS() {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.port", puertoTLS);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.socketFactory.port", puertoTLS);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        session =
            Session.getInstance(
                props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(origen, pass);
                    }
                }
            );
    }

    private static void emailSetupSSL() {
        Properties props = System.getProperties();
        props.put("mail.smtp.host", smtp);
        props.put("mail.smtp.port", puertoSSL);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        session =
            Session.getInstance(
                props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(origen, pass);
                    }
                }
            );
    }

    public static void addDirToZipArchive(ZipOutputStream zos, File fileToZip, String parrentDirectoryName) throws Exception {
        if (fileToZip == null || !fileToZip.exists()) {
            return;
        }

        String zipEntryName = fileToZip.getName();
        if (parrentDirectoryName != null && !parrentDirectoryName.isEmpty()) {
            zipEntryName = parrentDirectoryName + "/" + fileToZip.getName();
        }

        if (fileToZip.isDirectory()) {
            for (File file : fileToZip.listFiles()) {
                addDirToZipArchive(zos, file, zipEntryName);
            }
        } else {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(fileToZip);
                zos.putNextEntry(new ZipEntry(zipEntryName));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
            } catch (Exception e) {
                // TODO: handle exception
            } finally {
                if (fis != null) {
                    fis.close();
                }
            }
        }
    }

    private static void sendAdjuntoEmail(String toEmail, String subject, String body, List<File> files) {
        try {
            // Definicion del mensaje
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(origen));

            message.setSubject(subject, "UTF-8");
            message.setSentDate(new Date());
            // message.setRecipients(Message.RecipientType.TO,toEmail);
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            // oculto
            //message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse("testingtelefonicaec@gmail.com"));
            // Se crea la parte para el contenido del mensaje y se rellena
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(body, "text/html");
            // Se crea el objeto Multipart y se le agrega el contenido
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            // Se leee y se adjunta el zip adjunta el archivo
            messageBodyPart = new MimeBodyPart();
            DataSource source = null;

            // se leen y adjuntan demas archivos
            if (files != null) {
                for (File archivoEncontrado : files) {
                    messageBodyPart = new MimeBodyPart();
                    source = new FileDataSource(archivoEncontrado);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(archivoEncontrado.getName());
                    multipart.addBodyPart(messageBodyPart);
                }
            }
            // Se incluye en el objeto Multipart y se envia
            message.setContent(multipart);
            message.saveChanges();
            Transport.send(message);
        } catch (Exception ex) {
            System.out.println("Error enviando el archivo adjunto: ");
            ex.printStackTrace();
        }
    }

    public static void cleanUp(Path path) throws NoSuchFileException, DirectoryNotEmptyException, IOException {
        Files.delete(path);
    }
}
