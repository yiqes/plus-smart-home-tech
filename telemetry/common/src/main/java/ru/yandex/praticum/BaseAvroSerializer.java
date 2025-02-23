package ru.yandex.praticum;

import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumWriter;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class BaseAvroSerializer<T extends SpecificRecordBase> implements Serializer<T> {
    private final DatumWriter<T> datumWriter;

    // Конструктор принимает класс объекта, который будет сериализоваться
    public BaseAvroSerializer(Class<T> clazz) {
        this.datumWriter = new SpecificDatumWriter<>(clazz);
    }

    @Override
    public byte[] serialize(String topic, T data) {
        // Если данные null, возвращаем null
        if (data == null) {
            return null;
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Создаём бинарный энкодер для записи данных в поток
            Encoder encoder = EncoderFactory.get().binaryEncoder(out, null);
            // Записываем данные
            datumWriter.write(data, encoder);
            // Завершаем запись
            encoder.flush();
            // Возвращаем массив байтов
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сериализации Avro данных", e);
        }
    }
}
