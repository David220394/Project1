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
    MEDCINE {
        @Override
		public String getTitle() {
            return getStringFromResourceBundle("medcine.title");
        }

        @Override
		public String getFxmlFile() {
            return "/template/medcine.fxml";
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