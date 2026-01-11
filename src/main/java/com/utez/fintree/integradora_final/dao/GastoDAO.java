package com.utez.fintree.integradora_final.dao;

import com.utez.fintree.integradora_final.db.ConexionDB;
import com.utez.fintree.integradora_final.model.Gasto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GastoDAO {

    public List<Gasto> obtenerPorUsuario(int idUsuario) {
        List<Gasto> gastos = new ArrayList<>();
        String sql = "SELECT * FROM gastos WHERE id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Gasto gasto = new Gasto(
                        rs.getInt("id"),
                        rs.getString("concepto"),
                        rs.getDouble("monto"),
                        rs.getString("categoria"),
                        LocalDate.parse(rs.getString("fecha")),
                        rs.getString("metodo_pago"),
                        rs.getString("notas"),
                        rs.getInt("id_usuario")
                );
                gastos.add(gasto);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener los gastos: " + e.getMessage());
        }
        return gastos;
    }

    public boolean agregar(Gasto gasto, int idUsuario) {
        String sql = "INSERT INTO gastos(concepto, monto, categoria, fecha, metodo_pago, notas, id_usuario) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, gasto.getConcepto());
            pstmt.setDouble(2, gasto.getMonto());
            pstmt.setString(3, gasto.getCategoria());
            pstmt.setString(4, gasto.getFecha().toString());
            pstmt.setString(5, gasto.getMetodoPago());
            pstmt.setString(6, gasto.getNotas());
            pstmt.setInt(7, idUsuario);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al agregar el gasto: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Gasto gasto, int idUsuario) {
        String sql = "UPDATE gastos SET concepto = ?, monto = ?, categoria = ?, fecha = ?, metodo_pago = ?, notas = ? WHERE id = ? AND id_usuario = ?";

        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, gasto.getConcepto());
            pstmt.setDouble(2, gasto.getMonto());
            pstmt.setString(3, gasto.getCategoria());
            pstmt.setString(4, gasto.getFecha().toString());
            pstmt.setString(5, gasto.getMetodoPago());
            pstmt.setString(6, gasto.getNotas());
            pstmt.setInt(7, gasto.getId());
            pstmt.setInt(8, idUsuario);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar el gasto: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idGasto, int idUsuario) {
        String sql = "DELETE FROM gastos WHERE id = ? AND id_usuario = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idGasto);
            pstmt.setInt(2, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar el gasto: " + e.getMessage());
            return false;
        }
    }
}