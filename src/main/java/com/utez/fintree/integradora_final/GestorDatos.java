package com.utez.fintree.integradora_final;

import com.utez.fintree.integradora_final.dao.*;
import com.utez.fintree.integradora_final.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Optional;

public class GestorDatos {

    private static final GestorDatos instance = new GestorDatos();

    // Estado del usuario actual
    private Usuario usuarioActual;

    // Listas observables
    private final ObservableList<Ahorro> ahorros = FXCollections.observableArrayList();
    private final ObservableList<Gasto> gastos = FXCollections.observableArrayList();
    private final ObservableList<Presupuesto> presupuestos = FXCollections.observableArrayList();
    private final ObservableList<Categoria> categorias = FXCollections.observableArrayList();
    private final ObservableList<Deuda> deudas = FXCollections.observableArrayList();

    // DAOs
    private final AhorroDAO ahorroDAO;
    private final GastoDAO gastoDAO;
    private final PresupuestoDAO presupuestoDAO;
    private final CategoriaDAO categoriaDAO;
    private final DeudaDAO deudaDAO;

    private GestorDatos() {
        this.ahorroDAO = new AhorroDAO();
        this.gastoDAO = new GastoDAO();
        this.presupuestoDAO = new PresupuestoDAO();
        this.categoriaDAO = new CategoriaDAO();
        this.deudaDAO = new DeudaDAO();
    }

    public static GestorDatos getInstance() {
        return instance;
    }

    // --- GESTIÓN DE SESIÓN ---

    public void iniciarSesion(Usuario usuario) {
        this.usuarioActual = usuario;
        cargarDatosDeUsuario();
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
        ahorros.clear();
        gastos.clear();
        presupuestos.clear();
        categorias.clear();
        deudas.clear();
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    private void cargarDatosDeUsuario() {
        if (usuarioActual != null) {
            int id = usuarioActual.getId();
            ahorros.setAll(ahorroDAO.obtenerPorUsuario(id));
            gastos.setAll(gastoDAO.obtenerPorUsuario(id));
            presupuestos.setAll(presupuestoDAO.obtenerPorUsuario(id));
            categorias.setAll(categoriaDAO.obtenerPorUsuario(id));
            deudas.setAll(deudaDAO.obtenerPorUsuario(id));
        }
    }

    // --- GETTERS DE LISTAS ---
    public ObservableList<Ahorro> getAhorros() { return ahorros; }
    public ObservableList<Gasto> getGastos() { return gastos; }
    public ObservableList<Presupuesto> getPresupuestos() { return presupuestos; }
    public ObservableList<Categoria> getCategorias() { return categorias; }
    public ObservableList<Deuda> getDeudas() { return deudas; }

    // --- MÉTODOS DE MANIPULACIÓN DE DATOS ---

    public void agregarAhorro(Ahorro nuevoAhorro) {
        if (usuarioActual != null && ahorroDAO.agregar(nuevoAhorro, usuarioActual.getId())) {
            ahorros.setAll(ahorroDAO.obtenerPorUsuario(usuarioActual.getId()));
        }
    }

    public void actualizarAhorro(Ahorro ahorro) {
        if (usuarioActual != null && ahorroDAO.actualizar(ahorro, usuarioActual.getId())) {
            ahorros.setAll(ahorroDAO.obtenerPorUsuario(usuarioActual.getId()));
        }
    }

    public void agregarGasto(Gasto nuevoGasto) {
        if (usuarioActual != null && gastoDAO.agregar(nuevoGasto, usuarioActual.getId())) {
            gastos.setAll(gastoDAO.obtenerPorUsuario(usuarioActual.getId()));
        }
    }

    public void actualizarGasto(Gasto gasto) {
        if (usuarioActual != null && gastoDAO.actualizar(gasto, usuarioActual.getId())) {
            gastos.setAll(gastoDAO.obtenerPorUsuario(usuarioActual.getId()));
        }
    }

    public void agregarOActualizarPresupuesto(Presupuesto presupuesto) {
        if (usuarioActual != null) {
            boolean exito;
            Optional<Presupuesto> existente = presupuestos.stream()
                    .filter(p -> p.getMes() == presupuesto.getMes() && p.getAnio() == presupuesto.getAnio())
                    .findFirst();

            if (existente.isPresent()) {
                exito = presupuestoDAO.actualizar(presupuesto, usuarioActual.getId());
            } else {
                exito = presupuestoDAO.agregar(presupuesto, usuarioActual.getId());
            }

            if (exito) {
                presupuestos.setAll(presupuestoDAO.obtenerPorUsuario(usuarioActual.getId()));
            }
        }
    }

    public void agregarCategoria(Categoria categoria) {
        if (usuarioActual != null && categoriaDAO.agregar(categoria, usuarioActual.getId())) {
            categorias.setAll(categoriaDAO.obtenerPorUsuario(usuarioActual.getId()));
        }
    }

    public void actualizarCategoria(Categoria categoria) {
        if (usuarioActual != null && categoriaDAO.actualizar(categoria, usuarioActual.getId())) {
            categorias.setAll(categoriaDAO.obtenerPorUsuario(usuarioActual.getId()));
        }
    }

    public void eliminarCategoria(int idCategoria) {
        if (usuarioActual != null && categoriaDAO.eliminar(idCategoria, usuarioActual.getId())) {
            categorias.removeIf(cat -> cat.getId() == idCategoria);
        }
    }

    public void agregarDeuda(Deuda deuda) {
        if (usuarioActual != null && deudaDAO.agregar(deuda, usuarioActual.getId())) {
            deudas.setAll(deudaDAO.obtenerPorUsuario(usuarioActual.getId()));
        }
    }

    public void actualizarDeuda(Deuda deuda) {
        if (usuarioActual != null && deudaDAO.actualizar(deuda, usuarioActual.getId())) {
            deudas.setAll(deudaDAO.obtenerPorUsuario(usuarioActual.getId()));
        }
    }

    public void eliminarDeuda(int idDeuda) {
        if (usuarioActual != null && deudaDAO.eliminar(idDeuda, usuarioActual.getId())) {
            deudas.removeIf(d -> d.getId() == idDeuda);
        }
    }
}