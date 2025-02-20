package ru.yandex.practicum.deserializer;

import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

public class BaseAvroDeserializer<T extends SpecificRecordBase> implements Deserializer<T> {
    private final DecoderFactory decoderFactory;
    private final Schema schema;
    private final DatumReader<T> datumReader;

    // Конструктор с использованием DecoderFactory по умолчанию
    public BaseAvroDeserializer(Schema schema) {
        this(DecoderFactory.get(), schema);
    }

    // Конструктор с возможностью указать свою DecoderFactory
    public BaseAvroDeserializer(DecoderFactory decoderFactory, Schema schema) {
        this.decoderFactory = decoderFactory;
        this.schema = schema;
        this.datumReader = new SpecificDatumReader<>(schema);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            // Если данные отсутствуют, возвращаем null
            if (data == null) {
                return null;
            }
            // Создаем двоичный декодер из массива байтов
            BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
            // Читаем объект типа T из декодера
            return datumReader.read(null, decoder);
        } catch (Exception e) {
            // Обработка ошибок десериализации
            throw new RuntimeException("Ошибка десериализации данных Avro", e);
        }
    }
}