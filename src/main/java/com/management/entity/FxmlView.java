package com.management.entity;

import java.util.ResourceBundle;

public enum FxmlView {

    REGISTRATION {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("register.title");
        }

        @Override
		public String getFxmlFile() {
            return "/template/addPatient.fxml";
        }
    },
    NEW_APPOINTMENT {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("appointment.title");
        }

        @Override
		public String getFxmlFile() {
            return "/template/addConsultationDialog.fxml";
        }
    },
    MEDICINE {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("medicine.title");
        }

        @Override
		public String getFxmlFile() {
            return "/template/medicine.fxml";
        }
    },
    PROFILE {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("profile.title");
        }

        @Override
		public String getFxmlFile() {
            return "/template/profile.fxml";
        }
    },
    CONSULTATION {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("consultation.title");
        }

        @Override
		public String getFxmlFile() {
            return "/template/consultation.fxml";
        }
    },
    PRESCRIPTION {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("prescription.title");
        }

        @Override
		public String getFxmlFile() {
            return "/template/printPrescription.fxml";
        }
    },
    LIST {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("list.title");
        }

        @Override
        public String getFxmlFile() {
            return "/template/list.fxml";
        }
    },
    CALENDAR {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("calendar.title");
        }

        @Override
        public String getFxmlFile() {
            return "/template/calendar.fxml";
        }
    },
    INDEX {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("dashboard.title");
        }

        @Override
        public String getFxmlFile() {
            return "/template/index.fxml";
        }
    },
    HOME {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("home.title");
        }

        @Override
        public String getFxmlFile() {
            return "/template/home.fxml";
        }
    },
    DASHBOARD {
        @Override
        public String getTitle() {
            return getStringFromResourceBundle("dashboard.title");
        }

        @Override
        public String getFxmlFile() {
            return "/template/dashboard.fxml";
        }
    };
    
    public abstract String getTitle();
    public abstract String getFxmlFile();
    
    String getStringFromResourceBundle(String key){
        return ResourceBundle.getBundle("page").getString(key);
    }

}