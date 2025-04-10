    package com.example.signuploginrealtime;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.Button;
    import android.widget.TextView;

    import androidx.fragment.app.Fragment;

    public class sai_pooja_frag_nav_profile extends Fragment {

        TextView profileName, profileEmail, profileUsername, profilePassword, profileDepartment;
        TextView titleName, titleUsername;
        Button editProfile;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.sai_pooja_frag_nav_profile, container, false);

            profileName = view.findViewById(R.id.profileName);
            profileEmail = view.findViewById(R.id.profileEmail);
            profileUsername = view.findViewById(R.id.profileUsername);
            profilePassword = view.findViewById(R.id.profilePassword);
            titleName = view.findViewById(R.id.titleName);
            titleUsername = view.findViewById(R.id.titleUsername);
            editProfile = view.findViewById(R.id.editButton);
            profileDepartment = view.findViewById(R.id.profileDepartment1);
            loadUserData();

            editProfile.setOnClickListener(view1 -> passUserData());
            return view;
        }

        private void loadUserData() {
            UserDataSingleton userDataSingleton = UserDataSingleton.getInstance();
            HelperClass userData = userDataSingleton.getUserData();

            if (userData != null) {
                profileName.setText(userData.getName());
                profileEmail.setText(userData.getEmail());
                profileUsername.setText(userData.getUsername());
                profilePassword.setText(userData.getPassword());
                titleName.setText(userData.getName());
                titleUsername.setText(userData.getUsername());
                profileDepartment.setText(userData.getBranch());

            }
        }

        private void passUserData() {
            UserDataSingleton userDataSingleton = UserDataSingleton.getInstance();
            HelperClass userData = userDataSingleton.getUserData();

            if (userData != null) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("name", userData.getName());
                intent.putExtra("email", userData.getEmail());
                intent.putExtra("username", userData.getUsername());
                intent.putExtra("password", userData.getPassword());
                intent.putExtra("branch", userData.getBranch());
                startActivity(intent);
            }
        }
    }
