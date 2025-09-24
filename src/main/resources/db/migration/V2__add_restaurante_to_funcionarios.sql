
ALTER TABLE funcionarios
    ADD COLUMN restaurante_id BIGINT;


ALTER TABLE funcionarios
    ADD CONSTRAINT fk_funcionario_restaurante
    FOREIGN KEY (restaurante_id)
    REFERENCES restaurantes(id)
    ON DELETE CASCADE;


ALTER TABLE funcionarios
    ALTER COLUMN restaurante_id SET NOT NULL;
