package com.utez.fintree.integradora_final.dao;

import com.utez.fintree.integradora_final.db.ConexionDB;
import com.utez.fintree.integradora_final.model.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    public List<Categoria> obtenerPorUsuario(int idUsuario) {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre, color FROM categorias WHERE id_usuario = ? ORDER BY nombre";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Categoria categoria = new Categoria(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("color"),
                        idUsuario // Asignamos el id del usuario actual
                );
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las categorías: " + e.getMessage());
        }
        return categorias;
    }

    public boolean agregar(Categoria categoria, int idUsuario) {
        String sql = "INSERT INTO categorias(nombre, color, id_usuario) VALUES(?, ?, ?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getNombre());
            pstmt.setString(2, categoria.getColor());
            pstmt.setInt(3, idUsuario);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al agregar la categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Categoria categoria, int idUsuario) {
        String sql = "UPDATE categorias SET nombre = ?, color = ? WHERE id = ? AND id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, categoria.getNombre());
            pstmt.setString(2, categoria.getColor());
            pstmt.setInt(3, categoria.getId());
            pstmt.setInt(4, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la categoría: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idCategoria, int idUsuario) {
        String sql = "DELETE FROM categorias WHERE id = ? AND id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCategoria);
            pstmt.setInt(2, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar la categoría: " + e.getMessage());
            return false;
        }
    }
}