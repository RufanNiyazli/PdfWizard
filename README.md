
-----

## 📄 PDF Wizard Service

This project, **PDF Wizard Service**, is a powerful Spring Boot application designed to handle common PDF manipulation tasks such as **creation, splitting, and storage**, integrated with email functionality to deliver the processed documents.

It leverages the **Apache PDFBox** library for robust PDF processing and **Spring Mail** for email delivery.

### ✨ Features

  * **PDF Creation:** Generate new PDF documents from a structured JSON payload (Title, Paragraphs).
  * **PDF Splitting:** Split an uploaded PDF file into multiple smaller PDF files based on a specified number of pages per part.
  * **Email Delivery:** Automatically send processed or split PDF parts as attachments via email.
  * **Secure File Storage:** Utility for storing and retrieving temporary or processed files on the local file system.

### 🛠️ Technologies Used

  * **Java 21+**
  * **Spring Boot 3+**
  * **Apache PDFBox:** For all PDF generation and manipulation logic.
  * **Spring Data JPA / Hibernate:** For persistence (Database setup assumed for `Document` entity).
  * **Lombok:** To reduce boilerplate code.
  * **Jakarta Mail (Spring Mail):** For email functionality.

### 🚀 Getting Started

#### Prerequisites

  * Java Development Kit (JDK) 21 or later.
  * Maven or Gradle build tool.
  * A running **SMTP server** for the Email Service (e.g., Gmail, SendGrid, or a local server).

#### Configuration

1.  **Clone the Repository** (Assume this is your project location):

    ```bash
    git clone [your-repository-url]
    cd pdfwizard
    ```

2.  **Update `application.properties`** (or `application.yml`):

    You must configure the email service and the local file storage path.

    ```properties
    # --- Database Configuration (Example) ---
    spring.datasource.url=jdbc:postgresql://localhost:5432/pdfdb
    spring.datasource.username=user
    spring.datasource.password=password
    spring.jpa.hibernate.ddl-auto=update

    # --- Email Configuration ---
    spring.mail.host=smtp.example.com
    spring.mail.port=587
    spring.mail.username=your-email@example.com
    spring.mail.password=your-email-password
    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true

    # --- PDF File Storage Configuration ---
    # This is the directory where uploaded and generated files will be stored.
    pdf.location=./storage/pdfs
    ```

3.  **Run the Application:**

    ```bash
    ./mvnw spring-boot:run
    ```

    The application will start on `http://localhost:8080` (default port).

### 📖 API Endpoints

The PDF processing functionalities are exposed via REST endpoints, typically under the `/api/pdf` path.

| Method | Endpoint | Description | Request Body/Params |
| :--- | :--- | :--- | :--- |
| **POST** | `/api/pdf/create` | Generates a new PDF from a JSON body and returns the file. | **Body:** JSON object with `title` (String) and `paragraphs` (List\<String\>). |
| **POST** | `/api/pdf/split-and-email` | Splits an uploaded PDF into parts and emails the parts to a specified address. | **Params:** `file` (MultipartFile), `perPage` (int), `email` (String). |
| **POST** | `/api/pdf/upload` (Hypothetical) | Stores an uploaded PDF and logs its metadata to the database. | **Params:** `file` (MultipartFile), `email` (String). |

#### Example: PDF Creation Request

You can use a tool like Postman or `curl` to test the creation endpoint:

**Request:** `POST http://localhost:8080/api/pdf/create`

**Body (JSON):**

```json
{
    "title": "Project Summary Document",
    "paragraphs": [
        "This is the first paragraph of the generated document. It demonstrates basic text wrapping and line spacing.",
        "The second paragraph continues on a new line, ensuring that the content dynamically adjusts to the page boundaries using PDFBox's capabilities."
    ]
}
```

**Response:** Returns the generated `.pdf` file.

### 💡 Project Structure Overview

| File/Path | Description |
| :--- | :--- |
| `controller/PdfController.java` | REST endpoints for PDF creation, splitting, and file handling. |
| `service/IPdfService.java` | Interface for PDF processing logic. |
| `service/PdfServiceImpl.java` | Implementation using **Apache PDFBox** for creation and splitting. |
| `service/EmailService.java` | Service to send emails with attachments using **Spring Mail**. |
| `util/FileStorageService.java` | Utility to store and retrieve files on the local disk using the path configured in `pdf.location`. |
| `entity/Document.java` | JPA Entity for tracking stored PDF documents and their expiration. |
