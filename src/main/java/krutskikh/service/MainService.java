package krutskikh.service;

import krutskikh.calculation.CalculationFile;
import krutskikh.component.Bar;
import krutskikh.component.Construction;
import krutskikh.component.Joint;
import lombok.Getter;

import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MainService {
    private String path;

    public void save(Construction construction) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path)))
        {
            oos.writeObject(construction);
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void save(CalculationFile calculationFile, File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("Перемещения: " + calculationFile.getMoving() + "\n");
            writer.write("Продольные силы:" + calculationFile.getLongitudinalStrong() + "\n");
            writer.write("Нормальные напряжения:" + calculationFile.getNormalVoltage() + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
//        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path)))
//        {
//            oos.writeObject(calculationFile);
//        }
//        catch(Exception ex) {
//            System.out.println(ex.getMessage());
//        }
    }

    public Construction load() {
        Construction construction = new Construction();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path)))
        {
            construction = (Construction) ois.readObject();
        }
        catch(Exception ex) {

            System.out.println(ex.getMessage());
        }

        Joint.JOINT_COUNT = 1;
        List<Joint> newJoints = construction.getJoints().stream()
                .map(joint -> {
                    Joint res = new Joint();
                    res.setF(joint.getF());
                    res.setJointId(joint.getJointId());
                    res.recalculateInputs();

                    return res;
                })
                .collect(Collectors.toList());

        Bar.BAR_COUNT = 1;
        construction.setBars(construction.getBars().stream().map(bar -> {
            Bar res = new Bar();
            res.setBarId(res.getBarId());
            res.setL(bar.getL());
            res.setA(bar.getA());
            res.setQ(bar.getQ());
            res.setE(bar.getE());
            res.setSigma(bar.getSigma());
            res.recalculateInputs();

            res.setLeftJoint(newJoints.stream()
                    .filter(joint -> joint.getJointId().equals(bar.getLeftJoint().getJointId()))
                    .findAny().orElse(null));

            res.setRightJoint(newJoints.stream()
                    .filter(joint -> joint.getJointId().equals(bar.getRightJoint().getJointId()))
                    .findAny().orElse(null));

            return res;
        }).collect(Collectors.toList()));

        construction.setJoints(newJoints);

        return construction;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
