package com.example.chatApp.repository.file;

import com.example.chatApp.domain.Entity;
import com.example.chatApp.repository.memory.InMemoryRepository;
import java.io.*;
import java.util.Arrays;
import java.util.List;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;
    public AbstractFileRepository(String fileName) {
        this.fileName=fileName;
        loadData();

    }

    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  extract entity  - template method design pattern
     *  creates an entity of type E having a specified list of @code attributes
     * @param attributes
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);
    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e==null)
        {
            appendToFile(entity);
        }
        return e;

    }

    @Override
    public E update(E entity) {
        E e=super.update(entity);
        if(e != null) {
            writeToFile();
        }
        return e;
    }

    @Override
    public E delete(ID id) {
        E e = super.delete(id);
        if(e != null) {
            writeToFile();
        }
        return e;
    }

    protected void appendToFile(E entity){
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,true))) {
            bW.write(createEntityAsString(entity));
            bW.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeToFile() {
        try (BufferedWriter bW = new BufferedWriter(new FileWriter(fileName,false))) {
            for(E entity: findAll()) {
                bW.write(createEntityAsString(entity));
                bW.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

