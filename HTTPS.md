# HTTPS для Spring Boot

## Общие требования
- При генерации SSL-сертификата дополнительную информацию можно пропускать (нажимая Enter)
- Обязательные поля:
  - Пароль
  - DNS (в поле "first and last name" должен совпадать с DNS приложения)


## Генерация

### keystore
```bash
keytool -genkeypair -alias {придуманный alias} -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore {имя keystore}.p12 -validity 3650 -storepass {придуманный пароль}
```

### сертификата
```bash
keytool -export -keystore {имя keystore}.p12 -alias {придуманный alias} -file {имя сертификата}.crt
```

### проверь
```bash
keytool -list -keystore {имя keystore}.p12 -storepass {придуманный пароль}
```

## Настройка проекта

### 1. Разместить файлы
в папке `resources`:
   - `keystore.p12`
   - `{имя сертификата}.crt`

### 2. application.properties
```properties
server.ssl.enabled=true
server.ssl.protocol=TLS
server.ssl.enabled-protocols=TLSv1.3
server.ssl.key-store=classpath:keystore.p12
server.ssl.key-store-password={придуманный пароль}
server.ssl.key-store-type=PKCS12
server.ssl.key-alias={придуманный alias}
```
