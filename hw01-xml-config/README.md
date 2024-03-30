# ДЗ для "Введения в Spring Framework"

### Чек-лист для домашнего задания 
- [x] Все классы в приложении должны решать строго определённую задачу.
- [x] Контекст описывается XML-файлом.
- [x] Все зависимости должны быть настроены в IoC контейнере.
- [x] Имя ресурса с вопросами (CSV-файла) необходимо захардкодить строчкой в XML-файле с контекстом.
- [x] CSV с вопросами читается именно как ресурс, а не как файл.
- [x] Scanner, PrintStream и другие стандартные типы в контекст класть не нужно!
- [x] Весь ввод-вывод осуществляется на английском языке.
- [x] Крайне желательно написать юнит-тест какого-нибудь сервиса (оцениваться будет только попытка написать тест).
- [x] Приложение должно корректно запускаться с помощью "java -jar"

---
### Заметки

1. Возникли проблемы по заданию со звездочкой. Если не настраивать `AppendingTransformation` - возникает проблема [перезаписи](https://ru.stackoverflow.com/questions/866402/unable-to-locate-spring-namespacehandler-for-xml-schema). <p/>
    Чтобы этого не возникло - нужно вдобавок к указанию "запускающего класса" указать также дополнительные, конфигурирующие [аппендеры](https://maven.apache.org/plugins/maven-shade-plugin/examples/resource-transformers.html#AppendingTransformer:~:text=Merging%20Content%20of%20Specific%20Files%20with%20AppendingTransformer%2C%20XmlAppendingTransformer%20and%20ResourceBundleAppendingTransformer)
    
