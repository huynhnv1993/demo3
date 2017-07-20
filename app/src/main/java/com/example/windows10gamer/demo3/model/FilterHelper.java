package com.example.windows10gamer.demo3.model;

import android.widget.Filter;

import com.example.windows10gamer.demo3.adapter.AllContactsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Windows 10 Gamer on 19/07/2017.
 */

public class FilterHelper extends Filter {
    static List<ContactVO> currentList;
    static AllContactsAdapter allContactsAdapter;

    public static FilterHelper newInstance(List<ContactVO> currentList,AllContactsAdapter adapter){
        FilterHelper.allContactsAdapter = adapter;
        FilterHelper.currentList = currentList;
        return new FilterHelper();
    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults filterResults = new FilterResults();
        boolean check = true;
        if (constraint != null && constraint.length()>0){
            constraint = constraint.toString().toUpperCase();
            List<ContactVO> findList = new ArrayList();
            String contactname,contactnumber;

            for (int i = 0 ; i<currentList.size();i++){
                contactname = currentList.get(i).getContactName();
                contactnumber = currentList.get(i).getContactNumber();
                if (contactname.toUpperCase().contains(constraint) || contactnumber.toUpperCase().contains(constraint)){
                    findList.add(currentList.get(i));
                }
            }
            for (int i = 0 ; i<constraint.length();i++){
                if (Character.isDigit(constraint.charAt(i))){
                    continue;
                }else {
                    check = false;
                    break;
                }
            }
            if (check && constraint.length()>9 && constraint.length()<12 && findList.size()<1){
                ContactVO contactVO;
                contactVO = new ContactVO();
                contactVO.setContactName((String) constraint);
                contactVO.setContactNumber((String) constraint);
                findList.add(contactVO);
            }
            filterResults.count=findList.size();
            filterResults.values=findList;
        }else {
            filterResults.count = currentList.size();
            filterResults.values = currentList;
        }

        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        allContactsAdapter.setContactVOList((List<ContactVO>)results.values);
        allContactsAdapter.notifyDataSetChanged();
    }
}
